 package org.lexgrid.lexgraph.model;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.Document;

@Document("LexVertex")
public class LexVertex {
		
		@Id
		private String id;

		private String code;

		private String revision;

		private String namespace;
		
		private String description;

		public LexVertex(final String code, final String namespace, final String description) {
			this.code = code;
			this.namespace = namespace;
			this.description = description;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the revision
		 */
		public String getRevision() {
			return revision;
		}

		/**
		 * @param revision the revision to set
		 */
		public void setRevision(String revision) {
			this.revision = revision;
		}

		/**
		 * @return the namespace
		 */
		public String getNamespace() {
			return namespace;
		}

		/**
		 * @param namespace the namespace to set
		 */
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

	}